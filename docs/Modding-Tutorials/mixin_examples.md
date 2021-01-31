# Mixin Examples

This is a collection of frequently used mixins.

## Injecting into the head of a method

Mixin:

```java
@Inject(method = "foo()V", at = @At("HEAD"))
private void injected(CallbackInfo ci) {
  doSomething4();
}
```

Result:

```diff
  public void foo() {
+   injected(new CallbackInfo("foo", false));
    doSomething1();
    doSomething2();
    doSomething3();
  }
```

## Injecting into the tail of a method

Mixin:

```java
@Inject(method = "foo()V", at = @At("TAIL"))
private void injected(CallbackInfo ci) {
  doSomething4();
}
```

Result:

```diff
  public void foo() {
    doSomething1();
    if (doSomething2()) {
      return;
    }
    doSomething3();
+   injected(new CallbackInfo("foo", false));
  }
```

## Injecting into the returns of a method

Mixin:

```java
@Inject(method = "foo()V", at = @At("RETURN"))
private void injected(CallbackInfo ci) {
  doSomething4();
}
```

Result:

```diff
  public void foo() {
    doSomething1();
    if (doSomething2()) {
+     injected(new CallbackInfo("foo", false));
      return;
    }
    doSomething3();
+   injected(new CallbackInfo("foo", false));
  }
```

## Injecting into the point before a method call

Mixin:

```java
@Inject(method = "foo()V", at = @At(value = "INVOKE", target = "La/b/c/Something;doSomething()V"))
private void injected(CallbackInfo ci) {
  doSomething3();
}
```

Result:

```diff
  public void foo() {
    doSomething1();
    Something something = new Something();
+   injected(new CallbackInfo("foo", false));
    something.doSomething();
    doSomething2();
  }
```

## Injecting into the point after a method call

Mixin:

```java
@Inject(method = "foo()V", at = @At(value = "INVOKE", target = "La/b/c/Something;doSomething()V", shift = At.Shift.AFTER))
private void injected(CallbackInfo ci) {
  doSomething3();
}
```

Result:

```diff
  public void foo() {
    doSomething1();
    Something something = new Something();
    something.doSomething();
+   injected(new CallbackInfo("foo", false));
    doSomething2();
  }
```

## Injecting with a slice

Mixin:

```java
@Inject(
  method = "foo()V",
  at = @At(
    value = "INVOKE",
    target = "La/b/c/Something;doSomething()V"
  ),
  slice = @Slice(
    from = @At(value = "INVOKE", target = "La/b/c/Something;doSomething2()V"),
    to = @At(value = "INVOKE", target = "La/b/c/Something;doSomething3()V")
  )
)
private void injected(CallbackInfo ci) {
  doSomething5();
}
```

Result:

```diff
  public class Something {
    public void foo() {
      this.doSomething1();
+     // It will not inject into here because this is outside of the slice section
      this.doSomething();
      this.doSomething2();
+     injected(new CallbackInfo("foo", false));
      this.doSomething();
      this.doSomething3();
+     // It will not inject into here because this is outside of the slice section
      this.doSomething();
      this.doSomething4();
    }
  }
```

## Injecting and cancelling

Mixin:

```java
@Inject(method = "foo()V", at = @At("HEAD"), cancellable = true)
private void injected(CallbackInfo ci) {
  ci.cancel();
}
```

Result:

```diff
  public void foo() {
+   CallbackInfo ci = new CallbackInfo("foo", true);
+   injected(ci);
+   if (ci.isCancelled()) return;
    doSomething1();
    doSomething2();
    doSomething3();
  }
```

## Injecting and cancelling with a return value

Mixin:

```java
@Inject(method = "foo()I;", at = @At("HEAD"), cancellable = true)
private void injected(CallbackInfoReturnable<Integer> cir) {
  cir.setReturnValue(3);
}
```

Result:

```diff
  public int foo() {
+   CallbackInfoReturnable<Integer> cir = new CallbackInfoReturnable<Integer>("foo", true);
+   injected(cir);
+   if (cir.isCancelled()) return cir.getReturnValue();
    doSomething1();
    doSomething2();
    doSomething3();
    return 10;
  }
```

## Modifying a return value

Mixin:

```java
@Inject(method = "foo()I;", at = @At("RETURN"), cancellable = true)
private void injected(CallbackInfoReturnable<Integer> cir) {
  cir.setReturnValue(cir.getReturnValue() * 3);
}
```

Result:

```diff
  public int foo() {
    doSomething1();
    doSomething2();
-   return doSomething3() + 7;
+   int i = doSomething3() + 7;
+   CallbackInfoReturnable<Integer> cir = new CallbackInfoReturnable<Integer>("foo", true, i);
+   injected(cir);
+   if (cir.isCancelled()) return cir.getReturnValue();
+   return i;
  }
```

## Redirecting a method call

Mixin:

```java
@Redirect(method = "foo()V", at = @At(value = "INVOKE", target = "La/b/c/Something;doSomething(I)I"))
private int injected(Something something, int x) {
  return x + 3;
}
```

Result:

```diff
  public void foo() {
    doSomething1();
    Something something = new Something();
-   int i = something.doSomething(10);
+   int i = injected(something, 10);
    doSomething2();
  }
```

## Redirecting a get field

Mixin:

```java
@Redirect(method = "foo()V", at = @At(value = "FIELD", target = "La/b/c/Something;aaa:I", opcode = Opcodes.GETFIELD))
private int injected(Something something) {
  return 12345;
}
```

Result:

```diff
  public class Something {
    public int aaa;
    public void foo() {
      doSomething1();
-     if (this.aaa > doSomething2()) {
+     if (injected(this) > doSomething2()) {
        doSomething3();
      }
      doSomething4();
    }
  }
```

## Redirecting a put field

Mixin:

```java
@Redirect(method = "foo()V", at = @At(value = "FIELD", target = "La/b/c/Something;aaa:I", opcode = Opcodes.PUTFIELD))
private void injected(Something something, int x) {
  something.aaa = x + doSomething5();
}
```

Result:

```diff
  public class Something {
    public int aaa;
    public void foo() {
      doSomething1();
-     this.aaa = doSomething2() + doSomething3();
+     inject(this, doSomething2() + doSomething3());
      doSomething4();
    }
  }
```

## Modifying an argument

Mixin:

```java
@ModifyArg(method = "foo()V", at = @At(value = "INVOKE", target = "La/b/c/Something;doSomething(ZIII)V"), index = 2)
private int injected(int x) {
  return x * 3;
}
```

Result:

```diff
  public void foo() {
    doSomething1();
    Something something = new Something();
-   something.doSomething(true, 1, 4, 5);
+   something.doSomething(true, 1, injected(4), 5);
    doSomething2();
  }
```

## Modifying multiple arguments

Mixin:

```java
@ModifyArgs(method = "foo()V", at = @At(value = "INVOKE", target = "La/b/c/Something;doSomething(IDZ)V"))
private void injected(Args args) {
    int a0 = args.get(0);
    double a1 = args.get(1);
    boolean a2 = args.get(2);
    args.set(0, a0 + 3);
    args.set(1, a1 * 2.0D);
    args.set(2, !a2);
}
```

Result:

```diff
  public void foo() {
    doSomething1();
    Something something = new Something();
-   something.doSomething(3, 2.5D, true);
+   // Actually, synthetic subclass of Args is generated at runtime,
+   // but we omit the details to make it easier to understand the concept.
+   Args args = new Args(new Object[] { 3, 2.5D, true });
+   injected(args);
+   something.doSomething(args.get(0), args.get(1), args.get(2));
    doSomething2();
  }
```

## Modifying a parameter

Mixin:

```java
@ModifyVariable(method = "foo(ZIII)V", at = @At("HEAD"), ordinal = 1)
private int injected(int y) {
  return y * 3;
}
```

Result:

```diff
  public void foo(boolean b, int x, int y, int z) {
+   y = injected(y);
    doSomething1();
    doSomething2();
    doSomething3();
  }
```

## Modifying a local variable on an assignment

Mixin:

```java
@ModifyVariable(method = "foo()V", at = @At("STORE"), ordinal = 1)
private double injected(double x) {
  return x * 1.5D;
}
```

Result:

```diff
  public void foo() {
    int i0 = doSomething1();
    double d0 = doSomething2();
-   double d1 = doSomething3() + 0.8D;
+   double d1 = injected(doSomething3() + 0.8D);
    double d2 = doSomething4();
  }
```

