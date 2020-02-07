package com.picaproject.pica.Util;

// 퍼미션을 허용/비허용했을때 행동
public interface PermissioEventCallback {
    void OnPermit();
    void OnDenial();
}
