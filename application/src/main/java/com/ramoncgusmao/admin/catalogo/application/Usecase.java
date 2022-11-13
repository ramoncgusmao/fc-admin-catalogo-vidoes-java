package com.ramoncgusmao.admin.catalogo.application;


public abstract class Usecase<IN, OUT> {

    public abstract  OUT execute(IN anIN);

}
