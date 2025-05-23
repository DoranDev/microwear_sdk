//
//  NJY_HRBPBO2SetModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/18.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_HRBPBO2SetModel : NSObject
/**
 uint8_t hr; //心率
     uint8_t spo2; //血氧
     uint8_t sbp; //收缩压100~120（mmHg）
     uint8_t dbp; //舒张压60~80（mmHg）
 */
@property (nonatomic, assign) NSInteger hr;
@property (nonatomic, assign) NSInteger spo2;
@property (nonatomic, assign) NSInteger sbp;
@property (nonatomic, assign) NSInteger dbp;

@end

NS_ASSUME_NONNULL_END
