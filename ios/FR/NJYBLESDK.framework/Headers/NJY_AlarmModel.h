//
//  NJY_AlarmModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/18.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_AlarmModel : NSObject
/*
     ____ ____ ____ ____ ____ ____ ____ ____
    |     |      |       |        |     |      |       |        |
    |     |周六|周五|周四|周三|周二|周一|周日|
    |____|____|____|____|____|____|____|____|

    uint8_t state;            //开关状态
    alarm_remind_t type;     //0 ：没有 1 : 单次，2 ：每天，3自定义
    uint8_t    week_day;        //自定义7天，低7位表示(如上图)
    uint8_t hour;            //(0~23,24小时制)
    uint8_t min;            //(0~59)
 */
@property (nonatomic, assign) NSInteger aid;
@property (nonatomic, assign) NSInteger state;
@property (nonatomic, assign) NSInteger type;
@property (nonatomic, assign) NSInteger week_day;
@property (nonatomic, assign) NSInteger hour;
@property (nonatomic, assign) NSInteger min;

@end

NS_ASSUME_NONNULL_END
