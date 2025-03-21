//
//  NJY_NotifModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/18.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN
//状态：1：打开  0 ：关闭
@interface NJY_NotifModel : NSObject

@property (nonatomic, assign) NSInteger msgOther;
@property (nonatomic, assign) NSInteger msgCall;
@property (nonatomic, assign) NSInteger msgSms;
@property (nonatomic, assign) NSInteger msgWechat;
@property (nonatomic, assign) NSInteger msgqq;
@property (nonatomic, assign) NSInteger msgFackBook;
@property (nonatomic, assign) NSInteger msgMessenger;
@property (nonatomic, assign) NSInteger msgTwitter;
@property (nonatomic, assign) NSInteger msgInstagram;
@property (nonatomic, assign) NSInteger msgSkype;
@property (nonatomic, assign) NSInteger msgWhatsApp;
@property (nonatomic, assign) NSInteger msgLine;
@property (nonatomic, assign) NSInteger msgEmail;
@property (nonatomic, assign) NSInteger msgWeiBo;
@property (nonatomic, assign) NSInteger msgSnapchat;
@property (nonatomic, assign) NSInteger msgkakaotalk;
@end

NS_ASSUME_NONNULL_END
