package com.company;

@Deprecated
public class GenericTest<T extends Object> {

    private T innerObject;

    public void setInnerObject(T object) {
        this.innerObject = object;
    }

    public T getInnerObject() {
        return innerObject;
    }

    public <Q> Q doSomething(Q object1) {
        // object1...
        return object1;
    }

}
