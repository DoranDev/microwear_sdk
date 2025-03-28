//
//  NJY_WeatherModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/18.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, weatherType) {
    SUNNY  = 0,                                    //晴天
    CLOUDY,                                       //多云
    YIN,                                       //阴天
    RAIN,                                         //雨天
    SNOW,                                         //雪天
    FOG,                                          //雾天
    STORMS,                                       //沙尘暴
    HAZE,                                         //雾霾
};
@interface NJY_WeatherModel : NSObject

@property (nonatomic, assign) weatherType type;
@property (nonatomic, assign) NSInteger curTemp;

@end

NS_ASSUME_NONNULL_END
