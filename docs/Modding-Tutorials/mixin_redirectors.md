# Mixin redirectors

## introduction

Redirectors are methods that can replace method invocations, field
accesses, object creation, and `instanceof` checks. Redirectors are
declared by `@Redirect` annotations and generally look like this:

```java
@Redirect(method = "${signatureOfMethodInWhichToRedirect}",
          at = @At(value = "${injectionPointReference}", target = "${signature}"))
public ReturnType redirectSomeMethod(Arg0Type, arg0, Arg1Type arg1) {
    MyClass.doMyComputations();
  
    return computeSomethingElse();
}
```

Refer to the specific redirection tutorials for information about
injection point references:

- [redirecting methods](../Modding-Tutorials/mixin_redirectors_methods.md)
- [redirecting field access](../Modding-Tutorials/mixin_redirectors_fields.md)
- [redirecting object creation](../Modding-Tutorials/mixin_redirectors_constructors.md)
- [redirecting instanceof checks](../Modding-Tutorials/mixin_redirectors_instanceof.md)

