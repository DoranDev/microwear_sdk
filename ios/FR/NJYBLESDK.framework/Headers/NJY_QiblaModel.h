//
//  NJY_QiblaModel.h
//  NJYBLESDK
//
//  Created by Michael on 2023/12/21.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_QiblaModel : NSObject
@property (nonatomic, assign) NJY_Qibla qibaType;  //朝拜
@property (nonatomic, strong) NSArray *qibaList;  //朝拜
@property (nonatomic, assign) NSInteger direction;  //角度
@property (nonatomic, assign) NSInteger type;  //类型

//@property (nonatomic, assign) 
@end

NS_ASSUME_NONNULL_END
