# TestKotlinAndroidSynthetic
For illustrate the bug in https://youtrack.jetbrains.com/issue/KT-47733

## Environment
* Android Studio

Android Studio Arctic Fox | 2020.3.1 Beta 5

Build #AI-203.7717.56.2031.7479365, built on June 22, 2021

Runtime version: 11.0.10+0-b96-7281165 x86_64

VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.

macOS 11.4

GC: G1 Young Generation, G1 Old Generation

Memory: 2048M

Cores: 8

Registry: external.system.auto.import.disabled=true, debugger.watches.in.variables=false

Non-Bundled Plugins: Dart, wu.seal.tool.jsontokotlin, org.jetbrains.kotlin, io.flutter

* Jeb 

JEB 3.17.1.202004121849


## Reproduce Steps

1. Run the code using org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21
2. Get app-debug.apk in folder app/build/outputs/apk/debug
3. Decompile the app-debug.apk in Jeb and check MainActivity in package com.sheng.testkotlinversion
```
.method protected onCreate(Bundle)V
          .registers 4
          .param p1
          .end param
00000000  invoke-super        AppCompatActivity->onCreate(Bundle)V, p0, p1
00000006  const               v0, 0x7F0B001C  # layout:activity_main
0000000C  invoke-virtual      MainActivity->setContentView(I)V, p0, v0
00000012  sget                v0, R$id->tv_main:I
00000016  invoke-virtual      MainActivity->findViewById(I)View, p0, v0
0000001C  move-result-object  v0
0000001E  check-cast          v0, TextView
00000022  const-string        v1, "hello kotlin!"
00000026  check-cast          v1, CharSequence
0000002A  invoke-virtual      TextView->setText(CharSequence)V, v0, v1
00000030  return-void
.end method
```

4. change the plugin in file [build.gradle](https://github.com/shenguojun/TestKotlinAndroidSynthetic/blob/main/build.gradle), switch the kotlin-gradle-plugin version to 1.4.21
5. Rebuild and run the project
6. Get app-debug.apk in folder app/build/outputs/apk/debug
7. Decompile the app-debug.apk in Jeb and check MainActivity in package com.sheng.testkotlinversion
```
.method protected onCreate(Bundle)V
          .registers 4
          .param p1
          .end param
00000000  invoke-super        AppCompatActivity->onCreate(Bundle)V, p0, p1
00000006  const               v0, 0x7F0B001C  # layout:activity_main
0000000C  invoke-virtual      MainActivity->setContentView(I)V, p0, v0
00000012  sget                v0, R$id->tv_main:I
00000016  invoke-virtual      MainActivity->_$_findCachedViewById(I)View, p0, v0
0000001C  move-result-object  v0
0000001E  check-cast          v0, TextView
00000022  const-string        v1, "tv_main"
00000026  invoke-static       Intrinsics->checkNotNullExpressionValue(Object, String)V, v0, v1
0000002C  const-string        v1, "hello kotlin!"
00000030  check-cast          v1, CharSequence
00000034  invoke-virtual      TextView->setText(CharSequence)V, v0, v1
0000003A  return-void
.end method
```

8. Revert the kotlin gradle plugin version to 1.5.21
9. Uncomment below code in [app/build.gradle](https://github.com/shenguojun/TestKotlinAndroidSynthetic/blob/main/app/build.gradle)
```
tasks.withType(org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile) {
    kotlinOptions.useOldBackend = true
}
```
10. Rerun the project and get app-debug.apk in folder app/build/outputs/apk/debug
11. Decompile the app-debug.apk in Jeb and check MainActivity in package com.sheng.testkotlinversion
```
.method protected onCreate(Bundle)V
          .registers 4
          .param p1
          .end param
00000000  invoke-super        AppCompatActivity->onCreate(Bundle)V, p0, p1
00000006  const               v0, 0x7F0B001C  # layout:activity_main
0000000C  invoke-virtual      MainActivity->setContentView(I)V, p0, v0
00000012  sget                v0, R$id->tv_main:I
00000016  invoke-virtual      MainActivity->_$_findCachedViewById(I)View, p0, v0
0000001C  move-result-object  v0
0000001E  check-cast          v0, TextView
00000022  const-string        v1, "tv_main"
00000026  invoke-static       Intrinsics->checkNotNullExpressionValue(Object, String)V, v0, v1
0000002C  const-string        v1, "hello kotlin!"
00000030  check-cast          v1, CharSequence
00000034  invoke-virtual      TextView->setText(CharSequence)V, v0, v1
0000003A  return-void
.end method
```

We can see that when using kotlin android plugin with version 1.5.21, there is no `_$_findCachedViewById` generated.

But in kotlin android plugin 1.4.21 and in 1.5.21 with old backend there is `_$_findCachedViewById` generated.

I have put the output apk in folder [outputDebugApp](https://github.com/shenguojun/TestKotlinAndroidSynthetic/tree/main/outputDebugApp) and can decompile to see the difference.
