//
//  NJY_SharesModel.h
//  NJYBLESDK
//
//  Created by edison on 2024/5/30.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_SharesModel : NSObject

@property (nonatomic, strong) NSString *code;  //code
@property (nonatomic, strong) NSString *type; //sz、sh、hk、bj
@property (nonatomic, strong) NSString *currentPrice; //价格
@property (nonatomic, strong) NSString *changePercent; //涨幅
@property (nonatomic, strong) NSString *companyName; //名称
@property (nonatomic, assign) NSInteger number1; //数量
@property (nonatomic, assign) NSInteger currNumber; //当前数量

@end

NS_ASSUME_NONNULL_END
