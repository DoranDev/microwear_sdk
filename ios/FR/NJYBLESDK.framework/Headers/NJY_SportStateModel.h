//
//  NJY_SportStateModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/11/4.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_SportInfoModel : NSObject
@property (nonatomic, assign) NSInteger sport_state;        //状态
@property (nonatomic, assign) NSInteger sport_type;         //类型
@property (nonatomic, assign) NSInteger sport_gps;          //gps定位权限
@property (nonatomic, assign) NSInteger sport_hr;           //心率
@property (nonatomic, assign) NSInteger sport_valid;        //数据是否有效
@property (nonatomic, assign) NSInteger sport_cadence;           //步频
@property (nonatomic, assign) NSInteger sport_stride;           //步幅
@property (nonatomic, assign) NSInteger reserve3;           //扩展
@property (nonatomic, assign) NSInteger sport_time;         //时间    （秒）
@property (nonatomic, assign) NSInteger sport_steps;        //步数
@property (nonatomic, assign) NSInteger sport_kcal;         //卡路里（千卡）
@property (nonatomic, assign) NSInteger sport_distance;     //距离    （米）
@property (nonatomic, assign) NSInteger sport_speed;        //公里     （小时）

@end
@interface NJY_HrSectionModel : NSObject
@property (nonatomic, assign) NSInteger sectionType;        // 0: // 热身  1: // 燃脂  2: // 有氧 3: // 无氧 4: // 极限
@property (nonatomic, assign) NSInteger sectionValue;        //值

@end

@interface NJY_SportStateModel : NSObject
@property (nonatomic, assign) NSInteger cmdId; //GPS功能类型类型
@property (nonatomic, assign) NSInteger aid; //需要根据功能类型判断这个aid 运动ID 特殊：0为回复失败，1为回复验证成功
@property (nonatomic, strong) NJY_SportInfoModel *infoModel;
@property (nonatomic, strong) NSArray *matchSpeedList; //配速 同步为nil ota才设置值
@property (nonatomic, strong) NSArray <NJY_HrSectionModel *>* hrSectionList; //心率区间值
//hrSectionList 数组位置
//极限HR持续时间 (分钟)
//无氧HR持续时间 (分钟)
//有氧HR持续时间 (分钟)
//燃脂HR持续时间 (分钟)
//热身HR持续时间 (分钟)


@end

NS_ASSUME_NONNULL_END
