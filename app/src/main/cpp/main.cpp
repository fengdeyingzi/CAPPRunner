//加载系统头文件base.h
#include "src/base.h"
#include "graphics.h"
#include "android.h"
#include "src/mrc_base.h"



#ifdef __cplusplus
extern "C"{ //因为cpp文件默认定义了该宏),则采用C语言方式进行编译
#endif
void helloworld()
{
    printf("手机CAPP启动");

    LOGI("手机CAPP启动：%s","开始运行");
    LOGI("这是一个log");
    LOGE("这是一个错误日志");



    setscrsize(720, 720*SCRH/SCRW);
    //用指定颜色清除屏幕
    cls(0,0,0);
    //画文字
    dtext ( "风的影子", 0, 0, 255, 255, 255, 2, 1);
    dtext ("手机C论坛：bbs.yzjlb.net", 0, 60, 255,255,255, 2,1);
    //刷新屏幕
    ref(0,0,SCRW,SCRH);
    int *ma = (int*)malloc(20);
    printf("申请内存%x %x\n", ((int32)ma)^0xffffffff,ma);
    int aa = ((int)ma);
    aa ^= 0xffffffff;
    printf("%x\n",aa);
    aa ^= 0xffffffff;
    printf("%x\n",aa);
    free(ma);
}

void drawTest();

//入口函数，程序启动时开始执行
int init()
{
    //调用函数helloworld
    helloworld();

    return 0;
}



//event函数，接收消息事件
int event(int type, int p1, int p2)
{
    if(KY_UP == type)
    {
        switch(p1)
        {
            case _BACK:
                exit();
                break;
            case _MENU:
                break;
        }
    }

    return 0;
}

//应用暂停，当程序进入后台或变为不可见时会调用此函数
int pause()
{
    return 0;
}

//应用恢复，程序变为可见时调用此函数
int resume()
{
    return 0;
}

//应用退出函数，在应用退出时可做一些内存释放操作
int exitApp()
{
    return 0;
}

#ifdef __cplusplus
}
#endif