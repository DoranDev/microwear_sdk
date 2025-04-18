//
//  NJY_TypeMedicModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/10/14.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN
@interface medicTimeModel : NSObject

@property (nonatomic, assign) NSInteger hour; //24小时制
@property (nonatomic, assign) NSInteger min; //24小时制

@end

@interface NJY_TypeMedicModel : NSObject

@property (nonatomic, assign) bool state; //开关状态  1：打开  0：关闭
@property (nonatomic, assign) NSInteger week_day;//星期（遵守闹钟星期规则） 每天固定255
@property (nonatomic, strong) NSArray<medicTimeModel*>* timeList; //时间列表 组3组 空medicTimeModel 对位 hour=255；min =255

@end

NS_ASSUME_NONNULL_END
