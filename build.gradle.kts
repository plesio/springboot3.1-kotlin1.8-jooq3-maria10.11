import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
  id("org.springframework.boot") version "3.1.1"
  id("io.spring.dependency-management") version "1.1.0"
  id("nu.studer.jooq") version "8.2.1"
  id("org.flywaydb.flyway") version "9.20.0"
  id("org.openapi.generator") version "6.6.0"

  kotlin("jvm") version "1.8.22"
  kotlin("plugin.spring") version "1.8.22"
}

group = "saurus.plesio"
version = "0.0.1-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
  mavenCentral()
  maven { url = uri("https://jitpack.io") }
}

buildscript {
  dependencies {
    classpath("org.flywaydb:flyway-mysql:9.8.1")
  }
}

extra["testcontainersVersion"] = "1.18.3"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("com.github.guepardoapps:kulid:2.0.0.0")

  // -- DB
  runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
  runtimeOnly("com.mysql:mysql-connector-j:8.0.33") // for flyway
  implementation("org.flywaydb:flyway-mysql")

  // -- jOOQ
  implementation("org.springframework.boot:spring-boot-starter-jooq") {
    exclude(group = "org.jooq", module = "jooq")
  }
  implementation("org.jooq:jooq:3.18.5")
  jooqGenerator("com.mysql:mysql-connector-j:8.0.33")
  jooqGenerator("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")

  // -- OpenAPI
  compileOnly("io.swagger.core.v3:swagger-annotations:2.2.14")
  compileOnly("io.swagger.core.v3:swagger-models:2.2.14")
  compileOnly("jakarta.validation:jakarta.validation-api")
  compileOnly("jakarta.annotation:jakarta.annotation-api:2.1.1")

  // -- TEST
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.kotest:kotest-runner-junit5-jvm:5.6.2")
  testImplementation("io.kotest:kotest-assertions-core-jvm:5.6.2")
  testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
  testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:2.0.1")
  testImplementation("org.testcontainers:testcontainers:${property("testcontainersVersion")}")
  testImplementation("org.testcontainers:junit-jupiter:${property("testcontainersVersion")}")
  testImplementation("org.testcontainers:mariadb:${property("testcontainersVersion")}")
  testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310") // MEMO: 結果比較時に Java8のLocalDate型をパースできないため
}

dependencyManagement {
  imports {
    mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
  }
}


tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs += "-Xjsr305=strict"
    jvmTarget = "17"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

// docker-compose にも直書きしてあるので、こっちも直書きにする
// -- （本来は環境変数など非Git管理領域から取得する）
val dbUrlAsMySQL = "jdbc:mysql://localhost:3306/bookdb"
val dbUserName = "maria"
val dbPasswd = "mariaMaria"

flyway {
  url = dbUrlAsMySQL
  user = dbUserName
  password = dbPasswd
  cleanDisabled = false
  baselineVersion = "0.0.0"
  baselineOnMigrate = true
}

jooq {
  configurations {
    create("main") {
      // -- デフォルトで起動時ビルドをしないようにするおまじない。
      val isGenerate = System.getenv("JOOQ_GENERATE")?.toBoolean() ?: false
      generateSchemaSourceOnCompilation.set(isGenerate)
      // --
      jooqConfiguration.apply {
        jdbc.apply {
          url = dbUrlAsMySQL
          user = dbUserName
          password = dbPasswd
        }
        generator.apply {
          name = "org.jooq.codegen.KotlinGenerator"
          database.apply {
            name = "org.jooq.meta.mysql.MySQLDatabase"
            inputSchema = "bookdb"
            excludes = "flyway_schema_history"
          }
          generate.apply {
            isDeprecated = false
            isTables = true
            isRecords = true
            isPojos = true
            isDaos = true
            isValidationAnnotations = true
          }
          target.apply {
            packageName = "saurus.plesio.bookserver.jooq"
            directory = "${buildDir}/generated/source/jooq/main"
          }
          strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
        }
      }
    }
  }
}

// -- OpenAPI
task<GenerateTask>("generateApiServer") {
  generatorName.set("kotlin-spring")
  inputSpec.set("$projectDir/openapi_v1.yaml")
  outputDir.set("$buildDir/generated/source/openapi/server-code/")
  apiPackage.set("saurus.plesio.bookserver.openapi.generated.controller")
  modelPackage.set("saurus.plesio.bookserver.openapi.generated.model")
  configOptions.set(
    mapOf(
      "interfaceOnly" to "true",
      "useSpringBoot3" to "true",
      "serializableModel" to "true",
      "generatedConstructorWithRequiredArgs" to "false"
    )
  )
  additionalProperties.set(
    mapOf("useTags" to "true") //tags 準拠で、API の interface を生成する
  )
}

//Kotlinをコンパイルする前に、generateApiServerタスクを実行
tasks.compileKotlin {
  dependsOn("generateApiServer")
}

kotlin.sourceSets.main {
  kotlin.srcDir("$buildDir/generated/source/openapi/server-code/src/main")
}
