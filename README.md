dview-alert-dialog
![Release](https://jitpack.io/v/dora4/dview-alert-dialog.svg)
--------------------------------

#### Gradle依赖配置

```kotlin
// 添加以下代码到项目根目录下的build.gradle
allprojects {
    repositories {
        maven { setUrl("https://jitpack.io") }
    }
}
// 添加以下代码到app模块的build.gradle
dependencies {
    implementation("com.github.dora4:dview-alert-dialog:1.15")
}
```
#### 使用控件
```kotlin
DoraAlertDialog(this).show("提示信息") {
            title("系统消息")
            themeColorResId(R.color.colorAccent)
            positiveListener {
                showShortToast("点击了确认按钮")
            }
            negativeListener {
                showShortToast("点击了取消按钮")
            }
        }

```
