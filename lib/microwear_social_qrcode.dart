enum MicrowearSocialQrcode {
  weChat(0),
  qq(1),
  skype(2),
  facebook(3),
  twitter(4),
  whatsApp(5),
  line(6),
  instagram(7),
  messenger(8),
  snapchat(9);

  final int value;
  const MicrowearSocialQrcode(this.value);
}

class SocialQrcodeData {
  final int id;
  final String name;
  final String image;

  SocialQrcodeData({required this.id, required this.name, required this.image});
}

final List<SocialQrcodeData> socialQrcodeList = [
  SocialQrcodeData(
    id: 0,
    name: 'WeChat',
    image: 'https://cdn-icons-png.flaticon.com/512/3536/3536382.png',
  ),
  SocialQrcodeData(
    id: 1,
    name: 'QQ',
    image: 'https://cdn-icons-png.flaticon.com/512/1450/1450338.png',
  ),
  SocialQrcodeData(
    id: 2,
    name: 'Skype',
    image: 'https://cdn-icons-png.flaticon.com/512/2111/3536534.png',
  ),
  SocialQrcodeData(
    id: 3,
    name: 'Facebook',
    image: 'https://cdn-icons-png.flaticon.com/512/733/733547.png',
  ),
  SocialQrcodeData(
    id: 4,
    name: 'Twitter',
    image: 'https://cdn-icons-png.flaticon.com/512/733/733579.png',
  ),
  SocialQrcodeData(
    id: 5,
    name: 'WhatsApp',
    image: 'https://cdn-icons-png.flaticon.com/512/733/733585.png',
  ),
  SocialQrcodeData(
    id: 6,
    name: 'LINE',
    image: 'https://cdn-icons-png.flaticon.com/512/5968/5968754.png',
  ),
  SocialQrcodeData(
    id: 7,
    name: 'Instagram',
    image: 'https://cdn-icons-png.flaticon.com/512/2111/2111463.png',
  ),
  SocialQrcodeData(
    id: 8,
    name: 'Messenger',
    image: 'https://cdn-icons-png.flaticon.com/512/2111/2111728.png',
  ),
  SocialQrcodeData(
    id: 9,
    name: 'Snapchat',
    image: 'https://cdn-icons-png.flaticon.com/512/733/733635.png',
  ),
];
