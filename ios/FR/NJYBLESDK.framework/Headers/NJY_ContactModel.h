//
//  NJY_ContactModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/18.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_ContactModel : NSObject
@property (nonatomic, copy) NSString *name; // 姓名
@property (nonatomic, copy) NSString *num;  // 号码
@property (nonatomic, assign) BOOL isSosOn;
@property (nonatomic, assign) BOOL isSosShow;
@end

NS_ASSUME_NONNULL_END
