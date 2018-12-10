//自定义telephone校验规则
$.validator.addMethod("phoneFmt", function (value, element, param) {
    if (param) {
        //对手机号码进行合法性校验
        var telephoneReg = /^1[356789]\d{9}$/;
        if (telephoneReg.test(value)) {
            return true;
        } else {
            return false;
        }
    }
}, "请输入合法的手机号码");
