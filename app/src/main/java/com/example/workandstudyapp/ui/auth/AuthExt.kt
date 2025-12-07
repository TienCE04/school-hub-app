package com.example.workandstudyapp.ui.auth


fun checkAccountRegister(e:String,p:String,cp:String): Int{
    val regexEmail= Regex("""^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$""")
    val regexPassword = Regex("""^(?=.*\d)(?=.*[!@#$%^&*()_+\-={}|:;"'<>,.?/]).{6,}$""")
    if(e.isEmpty()){
        return -1
    }
    if(p.isEmpty()){
        return -2
    }
    if(p!=cp){
        return -3
    }
    if(!e.matches(regexEmail)){
        return -4
    }
    if(!p.matches(regexPassword)){
        return -5
    }

    return 1
}
