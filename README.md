dview-alert-dialog
![Release](https://jitpack.io/v/dora4/dview-alert-dialog.svg)
--------------------------------
![DORA视图 通告裁决者](https://github.com/user-attachments/assets/b591d3eb-29d2-4842-892d-37daac5a57d6)

##### 卡名：Dora视图 AlertDialog 
###### 卡片类型：效果怪兽
###### 属性：暗
###### 星级：7
###### 种族：魔法师族
###### 攻击力/防御力：2100/2500
###### 效果：此卡不会因为对方卡的效果而破坏，并可使其无效化。此卡攻击里侧守备表示的怪兽时，若攻击力高于其守备力，则给予对方此卡原攻击力的伤害，并抽一张卡。可以宣告一个种族或一个属性，只要此卡在场上存在，该种族或属性无法进行攻击宣言。

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
    implementation("com.github.dora4:dview-alert-dialog:1.30")
}
```
#### 使用控件
```kotlin
DoraAlertDialog.create(this).show("提示信息") {
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

#### 示例代码

https://github.com/dora4/dora_samples
