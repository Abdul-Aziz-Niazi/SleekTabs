# SleekTabs
Tab Layouts that support both horizontal and vertical orientations, tab gestures different font size for selected and non-selected tabs.

`min api level = 19`

# Version

[![](https://jitpack.io/v/Abdul-Aziz-Niazi/SleekTabs.svg)](https://jitpack.io/#Abdul-Aziz-Niazi/SleekTabs)

# Usage

For Layout

```XML
 <com.aziz.sleektablayout.SleekTabLayout
            android:id="@+id/tabs"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" 
            android:orientation="vertical"
            app:fontSize="14dp"
            app:gestureEnabled="true"
            app:layout_constraintTop_toBottomOf="@+id/toolbar"
            app:mode="compat" 
            app:selectedFontSize="38sp" 
            app:startingPosition="1" 
            app:textColor="@color/white50" />
```

```java
android:layout_height="wrap_content" //height is constant and can only be changed using mode
```

```java
app:mode="compat" // mode is height jumbo > cozy > compat
```

```java
android:orientation="vertical" // Orientation of tabs. Height of 'mode' is different for both orientations 
```

```java
app:fontSize="14dp" // non-seleted tabs font size DEFAULT 12
```

```java
app:selectedFontSize="38sp" // selected-tab font size DEFAULT 34
```

```java
app:startingPosition="1" // initial position DEFAULT 0
```
```java
app:gestureEnabled="true" // gestures to switch tabs. DEFAULT true 
```
In Code

```
tabs.setupWithViewPager(mViewPager);
```
# Preview

![alt text](https://github.com/Abdul-Aziz-Niazi/SleekTabs/blob/master/gif/horizontal.gif "Horizontal Orientation")

![alt text](https://github.com/Abdul-Aziz-Niazi/SleekTabs/blob/master/gif/vertical.gif "Vertical Orientation")


# Add to your Project
Just copy the following maven url in your Project Level `build.gradle` file.

```
allprojects {
  repositories {
	  ...
	  maven { url 'https://jitpack.io' }
  }
}
```
The following goes in your app level `build.gradle` dependencies
```
dependencies {
  ...
  implementation 'com.github.Abdul-Aziz-Niazi:SleekTabs:0.1.1'
}
```

# LICENSE

MIT License

Copyright (c) 2018 Abdul Aziz

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
