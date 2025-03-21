//
//  NJY_SysDataModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/18.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_SysDataModel : NSObject
//uint32_t      Steps;
//    uint32_t      Kcal;                //运动卡路里4Byte
//    uint32_t      Distance;            //运动距离4Byte
//    uint8_t     Hr;                 //心率1Byte
//    uint8_t     Spo2;               //血氧1Byte
//    uint8_t     Sbp;                //收缩压（mmHg）1Byte
//    uint8_t     Dbp;                //舒张压（mmHg）1Byte
//    uint8_t     Ecg;                 //心电

@property (nonatomic, assign) NSInteger steps;
@property (nonatomic, assign) NSInteger kcal;
@property (nonatomic, assign) NSInteger distance;
@property (nonatomic, assign) NSInteger hr;
@property (nonatomic, assign) NSInteger spo2;
@property (nonatomic, assign) NSInteger sbp;
@property (nonatomic, assign) NSInteger dbp;
@property (nonatomic, assign) NSInteger ecg;
@end

NS_ASSUME_NONNULL_END
