//
//  NJY_SportRecordModel.h
//  NJYBLESDK 
//
//  Created by Michael on 2022/7/18.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_SportRecordModel : NSObject
@property (nonatomic, assign) BCS_SPORT_T sportType;              //运动类型
@property (nonatomic, assign) NSInteger sportHr;                  // 运动hr
@property (nonatomic, assign) NSInteger sportAvgHr;               //运动平均hr
@property (nonatomic, assign) NSInteger sportCustom;              // 该字节作为预留
@property (nonatomic, assign) NSInteger startTimeStamp;           // 开始时间戳
@property (nonatomic, assign) NSInteger endTimeStamp;             // 结束时间戳
@property (nonatomic, assign) NSInteger sportTime;                // 运动时长（秒）
@property (nonatomic, assign) NSInteger sportSteps;               // 运动步数
@property (nonatomic, assign) NSInteger sportKcal;                // 运动卡路里
@property (nonatomic, assign) NSInteger sportDistance;            //运动距离
@property (nonatomic, strong) NSArray *sportHrList;               //hrList

@end

NS_ASSUME_NONNULL_END
