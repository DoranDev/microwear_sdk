//
//  NJY_AReceiptCodeModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/18.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_AReceiptCodeModel : NSObject
@property (nonatomic, assign) NSInteger evtType;
@property (nonatomic, assign) NSInteger type;
@property (nonatomic, strong) NSString *str;

@end

NS_ASSUME_NONNULL_END
