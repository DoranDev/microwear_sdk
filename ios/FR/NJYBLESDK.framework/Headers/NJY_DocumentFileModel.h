//
//  NJY_DocumentFileModel.h
//  NJYBLESDK
//
//  Created by Michael on 2023/4/19.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_DocumentFileModel : NSObject
@property (nonatomic, assign) NSInteger curId;     //文件id
@property (nonatomic, assign) NSInteger totalId;   //总列表数量
@property (nonatomic, strong) NSString *fileName;        //名称
@property (nonatomic, assign) NSInteger fileTime;        //时间

@end

NS_ASSUME_NONNULL_END
