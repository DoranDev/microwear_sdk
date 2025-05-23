//
//  NJY_PublicEnum.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/20.
//

#import <Foundation/Foundation.h>
NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, EVT_OTA_TYPE) {
    EVT_TYPE_OTAMODE                = 0,/*普通升级*/
    EVT_TYPE_UPGRADES_LIENTLY        ,/*静默升级*/
};
typedef NS_ENUM(NSInteger, EVT_DEVICE_TYPE) {
    EVT_DEVICE_RY                = 0,/*瑞昱*/
    EVT_DEVICE_JL                ,/*杰里*/
};
typedef NS_ENUM(NSInteger, BCS_SPORT_T) {
    sport_run             = 0,  //户外跑
    sport_runindoor       = 1,  //室内跑
    sport_walk            = 2,  //走路
    sport_yoga            = 3,  //瑜伽
    sport_bicycle         = 4,  //自行车
    sport_mountain        = 5,  //徒步爬山
    sport_pingpang        = 6, //乒乓球
    sport_badminton       = 7,  //羽毛球
    sport_basketball      = 8,  //篮球
    sport_football        = 9,  //足球
    sport_skip            = 10,  //跳绳
    sport_swim            = 11,  //游泳
    sport_tennis         = 12, //网球
    sport_weight         = 13,  //举重
    sport_marathon       = 14, //马拉松
    sport_pool_swim               = 15 , //泳池游泳
        sport_rugby                   = 16 , //橄榄球
        sport_golf                    = 17 , //高尔夫
        sport_fitness                 = 18 , //健身
        sport_dance                   = 19 , //跳舞
        sport_baseball                = 20 , //棒球
        sport_elliptical              = 21 , //椭圆机
        sport_indoor_cycle            = 22 , //室内单车
        sport_free_training           = 23 , //自由训练
        sport_rower                   = 24 , //划船机
        sport_sailing                 = 25 , //帆船运动
        sport_water_polo              = 26 , //水球
        sport_water_sports            = 27 , //水上运动
        sport_paddle_boarding         = 28 , //浆板
        sport_water_skiing            = 29 , //划水
        sport_padding                 = 30 , //划艇
        sport_rafting                 = 31 , //皮艇漂流
        sport_rowing                  = 32 , //划船
        sport_powerboating            = 33 , //摩托艇
        sport_finswimming             = 34 , //蹼泳
        sport_diving                  = 35 , //跳水
        sport_artistic_swimming       = 36 , //花样游泳
        sport_snorkeling              = 37 , //浮潜
        sport_surfing                 = 38 , //冲浪
        sport_open_water_swim         = 39 , //开放水域游泳
        sport_climbing                = 40 , //攀岩
        sport_skateboarding           = 41 , //滑板
        sport_roller_skating          = 42 , //轮滑
        sport_parkour                 = 43 , //跑酷
        sport_atv                     = 44 , //沙滩车
        sport_paragliding             = 45 , //滑翔伞
        sport_triathlon               = 46 , //铁人三项
        sport_trail_running           = 47 , //越野跑
        sport_hiking                  = 48 , //徒步
        sport_kabaddi                 = 49 , //卡巴迪
        sport_step_training           = 50 , //踏步训练
        sport_stairs                  = 51 , //楼梯
        sport_stair_stepper           = 52 , //踏步机
        sport_core_training           = 53 , //核心训练
        sport_flexibility             = 54 , //柔韧度
        sport_pilates                 = 55 , //普拉提
        sport_gymnastics              = 56 , //体操
        sport_stretching              = 57 , //拉伸
        sport_strength                = 58 , //功能性力量训练
        sport_cross_training          = 59 , //交叉训练
        sport_aerobics                = 60 , //健身操
        sport_physical_training       = 61 , //体能训练
        sport_wall_ball               = 62 , //墙球
        sport_dumbbells               = 63 , //哑铃训练
        sport_barbell                 = 64 , //杠铃训练
        sport_deadlift                = 65 , //硬拉
        sport_upper_body              = 66 , //上肢训练
        sport_sit_ups                 = 67 , //仰卧起坐
        sport_functional_training     = 68 , //功能性训练
        sport_burpee                  = 69 , //波比跳
        sport_back                    = 70 , //背部训练
        sport_lower_body              = 71 , //下肢训练
        sport_abs                     = 72 , //腰腹训练
        sport_hiit                    = 73 , //高强度间歇训练
        sport_indoor_walk             = 74 , //室内步行
        sport_indoor_run              = 75 , //室内跑步
        sport_social_dance            = 76 , //社交舞
        sport_belly_dance             = 77 , //肚皮舞
        sport_ballet                  = 78 , //芭蕾
        sport_zumba                   = 79 , //尊巴
        sport_latin_dance             = 80 , //拉丁舞
        sport_street_dance            = 81 , //街舞
        sport_folk_dance              = 82 , //民族舞
        sport_jazz_dance              = 83 , //爵士舞
        sport_boxing                  = 84 , //拳击
        sport_wrestling               = 85 , //摔跤
        sport_martial_arts            = 86 , //武术
        sport_tai_chi                 = 87 , //太极
        sport_muay_thai               = 88 , //泰拳
        sport_judo                    = 89 , //柔道
        sport_taekwondo               = 90  , //跆拳道
        sport_karate                  = 91  , //空手道
        sport_kickboxing              = 92  , //自由搏击
        sport_fencing                 = 93  , //剑术
        sport_kendo                   = 94  , //剑道
        sport_volleyball              = 95  , //排球
        sport_softball                = 96  , //垒球
        sport_hockey                  = 97  , //曲棍球
        sport_cricket                 = 98  , //板球
        sport_handball                = 99  , //手球
        sport_squash                  = 100 , //壁球
        sport_billiards               = 101 , //台球
        sport_shuttlecock             = 102 , //毽球
        sport_beach_soccer            = 103 , //沙滩足球
        sport_beach_volleyball        = 104 , //沙滩排球
        sport_sepak_takraw            = 105 , //藤球
        sport_bowling                 = 106 , //保龄球
        sport_skating                 = 107 , //滑冰
        sport_curling                 = 108 , //冰壶
        sport_snow_sports             = 109 , //雪上运动
        sport_snowmobile              = 110 , //雪地摩托
        sport_ice_hockey              = 111 , //冰球
        sport_bobsleigh               = 112 , //雪车
        sport_sledding                = 113 , //雪橇
        sport_skiing                  = 114 , //滑雪å
        sport_archery                 = 115 , //射箭
        sport_darts                   = 116 , //飞镖
        sport_tug_of_war              = 117 , //拔河
        sport_hula_hoop               = 118 , //呼啦圈
        sport_kite_flying             = 119 , //放风筝
        sport_house_riding            = 120 , //骑马
        sport_disc_sports             = 121 , //飞盘运动
        sport_fishing                 = 122 , //钓鱼
        sport_equestrian_sports       = 123 , //马术运动
        sport_track_and_field         = 124 , //田径
        sport_auto_racing             = 125 , //赛车
    
};

typedef NS_ENUM(NSInteger, NJY_Language) {
    AALanguage_en = 0,     //英语
    AALanguage_cn,         //中文
    AALanguage_es,         //西班牙语
    AALanguage_pt,         //葡萄牙语
    AALanguage_ar,         //阿拉伯
    AALanguage_el,         //希腊语       ，Ελληνικά
    AALanguage_hu,         //匈牙利语     ，Magyar
    AALanguage_pl,         //波兰语       ，Polski
    AALanguage_th,         //泰文
    AALanguage_ja,         //日语
    AALanguage_ko,         //韩文
    AALanguage_id,         //印尼         ，Indonesia
    AALanguage_fr,          //法语
    AALanguage_de,          //德语
    AALanguage_cs,          //捷克         ，čeština
    AALanguage_nl,          //荷兰语,
    AALanguage_tr,          //土耳其语     ，Türk
    AALanguage_it,          //意大利语
    AALanguage_ru,          //俄语
    AALanguage_fa,          //波斯语       ，فارسی
    AALanguage_vi,          //越南
    AALanguage_uk,          //乌克兰
    AALanguage_iw,          //希伯来       ，עִברִית
    AALanguage_knk,          //朝鲜       ，עִברִית
    AALanguage_ma,          //马来西亚       ，עִברִית
    AALanguage_sl,          //斯洛伐克       ，עִברִית
    AALanguage_sj,          //斯洛文尼亚       ，עִברִית
    AALanguage_cht, //繁体
    
    AALang_ro,    //罗马尼亚     ，limba română
    AALang_bg,    //保加利亚     ，Български език
    AALang_hr,    //克罗地亚     ，Hrvatski
    AALang_et ,    //爱沙尼亚     ，Eesti keel‎
    AALang_sr,    //塞尔维亚     ，Cрпски
    AALang_sv ,    //瑞典         ，svenska
    AALang_fi,    //芬兰         ，Suomen Kieli
    AALang_lv,    //拉脱维亚     ，latviešu valoda‎
    
};

typedef NS_ENUM(NSInteger, NJY_Qibla) {
    AAFajr = 0,
    Sunrise,
    Dhuhr,
    Ast,
    Sunset,
    Maghrib,
    Isha,
    Imsak,
    Midnight,
    Firstthird,
    Lastthird,
//    0:Fajr
//    1: Sunrise
//    2: Dhuhr
//    3: Ast
//    4: Sunset
//    5: Maghrib
//    6: Isha
//    7: Imsak
//    8: Midnight
//    9: Firstthird
//    10: Lastthird
};
@interface NJY_PublicEnum : NSObject


@end

NS_ASSUME_NONNULL_END
