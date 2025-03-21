//
//  NJYAsyncCallback.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/18.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJYAsyncCallback <ObjectType>  : NSObject
+ (instancetype)create:(id _Nullable)holder success:(void (^)(ObjectType result))success failure:(void (^)(NSError* error))failure;
+ (instancetype)create:(id _Nullable)holder success:(void (^)(ObjectType result))success progress:(void (^)(float progress))progress failure:(void (^)(NSError* error))failure;

- (void)sendSuccessMessage:(ObjectType)result;
- (void)sendFailureMessage:(NSError*)error;
- (void)sendProgressMessage:(float)progress;

- (void)clearCallBack;
@end
NS_ASSUME_NONNULL_END
