//
//  NJY_WeatherForecastModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/11/10.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN
 
typedef NS_ENUM(NSInteger, wind_diryType) {
    wind_NONE           = 0,     //无
    wind_east           = 1,     //东
    wind_southeast      = 2,     //东南
    wind_south          = 3,     //南
    wind_southwest      = 4,     //西南
    wind_west           = 5,     //西
    wind_northwest      = 6,     //西北
    wind_north          = 7,     //北
    wind_northeast      = 8,     //东北
};

@interface NJY_WeatherForecastInfoModel : NSObject
 //week 星期 星期天 0 ;周一 1 ；周二 2；周三 3 ；周四 4；周五:5 周 六 :6
@property (nonatomic, assign) NSInteger  week;//星期 
@property (nonatomic, assign) NSInteger type;//天气类型
@property (nonatomic, assign) NSInteger temp_high;    //高温
@property (nonatomic, assign) NSInteger temp_low;     //低温
@property (nonatomic, assign) NSInteger pressure;     //气压
@property (nonatomic, assign) NSInteger ult_level;    //紫外线等级
@property (nonatomic, assign) NSInteger         humidity;                       //湿度
@property (nonatomic, assign) wind_diryType         wind_dir;                       //风向
@property (nonatomic, assign) NSInteger         wind_lvl;                       //风等级
@property (nonatomic, assign) NSInteger        visibility;                     //能见度   米
@property (nonatomic, assign) NSInteger        precipitation;                  //降雨量   毫米
@property (nonatomic, assign) NSInteger     reserve;                      //预留
@property (nonatomic, assign) NSInteger     reserve1;                      //预留1
@property (nonatomic, strong) NSString     *reserve2;                      //预留2

@end

@interface NJY_WeatherForecastModel : NSObject
@property (nonatomic, strong) NSArray<NJY_WeatherForecastInfoModel*> *infoModelList;
@end

NS_ASSUME_NONNULL_END
