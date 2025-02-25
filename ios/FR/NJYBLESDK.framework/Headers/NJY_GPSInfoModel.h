//
//  NJY_GPSInfoModel.h
//  NJYBLESDK
//
//  Created by edison on 2024/12/11.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_GPSInfoModel : NSObject
@property (nonatomic, assign) NSInteger timeInterval;
@property (nonatomic, strong) NSArray *paceList;
@property (nonatomic, strong) NSArray *matchSpeedList;
@property (nonatomic, strong) NSArray *coordinateList;
 
@end

NS_ASSUME_NONNULL_END
