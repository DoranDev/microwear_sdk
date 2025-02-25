//
//  NJY_SchedDuleModel.h
//  NJYBLESDK
//
//  Created by edison on 2024/11/4.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_SchedDuleModel : NSObject

@property (nonatomic, assign) NSInteger id;
@property (nonatomic, assign) NSInteger state;          //1 预留 0关闭，1开启2删除
@property (nonatomic, assign) NSInteger remind_out;     //1 周期间隔
@property (nonatomic, assign) NSInteger startTime;      //1 提醒时间
@property (nonatomic, strong) NSString *title;          //标題
@property (nonatomic, strong) NSString *content;        //备汪字节息长度不能超过 100 Byt

@end

NS_ASSUME_NONNULL_END
