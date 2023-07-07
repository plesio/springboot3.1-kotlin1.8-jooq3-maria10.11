package saurus.plesio.bookserver.util

fun generateAuthorName(): String {
  return "${firstNames.random()} ${lastNames.random()}"
}


val lastNames: Array<String> = arrayOf(
  "田中", "山田", "佐藤", "鈴木", "高橋", "渡辺", "伊藤", "山口", "松本", "井上",
  "木村", "小林", "加藤", "吉田", "山本", "中村", "小川", "大野", "三浦", "阿部",
  "山崎", "中島", "石川", "原田", "柴田", "酒井", "工藤", "横山", "宮崎", "西村",
  "谷口", "金子", "藤本", "岡田", "上田", "中野", "坂本", "村上", "田村", "長谷川",
  "竹内", "石井", "青木", "森田", "原", "森", "池田", "齋藤", "岡本", "橋本",
  "山下", "菅原", "佐々木", "平野", "大西", "野口", "松田", "川口", "小野", "松下",
  "木下", "菊地", "村田", "大塚", "浜田", "平田", "菊池", "荒木", "大谷", "小島",
  "及川", "今井", "中西", "河野", "堀", "石田", "岩崎", "前田", "松村", "谷川",
  "福田", "小山", "坂口", "水野", "北村", "西田", "片山", "原口", "多田", "中田",
  "五十嵐", "川崎", "飯田", "笠原", "内田", "平井", "田口", "新井", "渡部", "黒田",
  "篠崎", "小松", "小西", "鎌田", "宮本", "橋田", "石原", "秋山", "熊谷", "樋口",
  "望月", "尾崎", "朝倉", "川上", "松井", "内藤", "岡村", "片岡", "向井", "大槻",
  "栗原", "荒井", "江口", "浅野", "久保", "中尾", "今村", "鎌倉", "堀田", "松岡",
  "小沢", "田辺", "吉村", "石崎", "畑中", "坂井", "杉山", "松浦", "大島", "平山",
  "大久保", "杉本", "高田", "川村", "小田", "菅野", "大石", "小池", "関口", "石塚",
  "大沢", "小川", "大森", "大橋", "松原", "松尾", "大崎", "小泉", "野村", "松永",
  "松原", "松尾", "大崎", "小泉", "野村", "松永", "松原", "松尾", "大崎", "小泉",
)

val firstNames: Array<String> = arrayOf(
  "太郎", "次郎", "三郎", "四郎", "五郎", "六郎", "七郎", "八郎", "九郎", "十郎",
  "花子", "次郎", "美紀", "健太", "麻衣", "雅彦", "恵子", "哲也", "由美",
  "大輔", "千代子", "拓也", "真理子", "光男", "香織", "宏明", "裕子", "剛志", "絵美",
  "慎太郎", "千晴", "宗太", "奈美子", "隆太郎", "亜美", "将之", "理恵", "悠太", "佳奈子",
  "健太郎", "美晴", "大輔", "千代子", "拓也", "真理子", "光男", "香織", "宏明", "裕子",
  "剛志", "絵美", "慎太郎", "千晴", "宗太", "奈美子", "隆太郎", "亜美", "将之", "理恵",
  "悠太", "佳奈子", "健太郎", "美晴", "大輔", "千代子", "拓也", "真理子", "光男", "香織",
  "宏明", "裕子", "剛志", "絵美", "慎太郎", "千晴", "宗太", "奈美子", "隆太郎", "亜美",
  "将之", "理恵", "悠太", "佳奈子", "健太郎", "美晴", "大輔", "千代子", "拓也", "真理子",
  "光男", "香織", "宏明", "裕子", "剛志", "絵美", "慎太郎", "千晴", "宗太", "奈美子",
)