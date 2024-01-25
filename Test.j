.class public Test
.super java/lang/Object

.field public static print I
.field public static a I
.method static <clinit>()V
ldc 0
.limit stack 32
.limit locals 32
return
.end method

.method public static sum(III)I
.limit stack 32
.limit locals 32
iload_0
ldc 1
if_icmpne L0
ldc 1
ireturn
goto L3
L0:
iload_0
ldc 2
if_icmpne L1
iload_1
ldc 2
if_icmpne L10
ldc 20
istore_1
iload_0
iload_1
iload_2
iadd
iadd
ireturn
goto L11
L10:
ldc 200
istore_1
iload_0
iload_1
iload_2
iadd
iadd
ireturn
goto L11
L11:
goto L3
L1:
iload_0
ldc 2
if_icmpne L2
ldc 2
ireturn
goto L3
L2:
ldc 0
ireturn
goto L3
L3:
return
.end method

.method public static main([Ljava/lang/String;)V
.limit stack 32
.limit locals 32
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc 2
ldc 2
ldc 3
invokestatic Test/sum(III)I
invokevirtual java/io/PrintStream/println(I)V
ldc 0
putstatic Test/a I
L0:
getstatic Test/a I
ldc 10
if_icmpge L0@
getstatic java/lang/System/out Ljava/io/PrintStream;
getstatic Test/a I
invokevirtual java/io/PrintStream/println(I)V
getstatic Test/a I
ldc 1
iadd
putstatic Test/a I
goto L0
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc 20
invokevirtual java/io/PrintStream/println(I)V
goto L0
L0@:
return
.end method

; standard initializer
.method public <init>()V
aload_0
invokespecial java/lang/Object/<init>()V
return
.end method

