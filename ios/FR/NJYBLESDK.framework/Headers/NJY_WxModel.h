//
//  NJY_WxModel.h
//  NJYBLESDK
//
//  Created by NJY_Macmini on 2025/3/14.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_WxModelInfo : NSObject
@property (nonatomic, strong) NSDictionary *desDic;          //聊天记录
@property (nonatomic, strong) NSData *contextData;          //聊天记录

@end

@interface NJY_WxModel : NSObject
@property (nonatomic, assign) NSInteger type;      //请求类型
//WECHAT QRCODE =1 二维码
//WECHAT LOGIN STATE= 2  成功失败
//WECHAT USER LIST = 3,

@property (nonatomic, strong) NSString *url;          //url
@property (nonatomic, assign) NSInteger state;          //状态
//@property (nonatomic, assign) NSInteger state;          //状态
@property (nonatomic, strong) NSArray<NJY_WxModelInfo*> *msgList;          //信息列表

@property (nonatomic, strong) NSString *uuid;          //扫描ID
@property (nonatomic, strong) NSString *wxuuid;          //微信ID

@property (nonatomic, strong) NSDictionary *desDic;          //聊天记录


@property (nonatomic, strong) NSString *acceptWxuuid;          //收件人wxID
@property (nonatomic, strong) NSString *sendContent;          //内容
@property (nonatomic, strong) NSString *filePath;          //语音路径
@property (nonatomic, assign) NSTimeInterval voiceTime;          //语音长度
@property (nonatomic, strong) NSData *contextData;          //聊天记录
@property (nonatomic, assign) NSInteger contextPage;          //多少包
@property (nonatomic, assign) NSInteger createTime;          //时间戳


//[mutableDic setObject:userName forKey:@"FromUserName"];
//[mutableDic setObject:nickName forKey:@"ToUserName"];
//[mutableDic setObject:content forKey:@"Content"];
//NSString *formName =  [formName1 componentsSeparatedByString:@":"].firstObject;
//[mutableDic setObject:formName forKey:@"FormName"];

//[mutableDic setObject:CreateTime forKey:@"CreateTime"];

@end


NS_ASSUME_NONNULL_END
