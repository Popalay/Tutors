[![Download](https://api.bintray.com/packages/popalay/maven/Tutors/images/download.svg) ](https://bintray.com/popalay/maven/Tutors/_latestVersion)
[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg)](https://github.com/Popalay/Tutors/blob/master/LICENSE)

# Tutors

A simple way to show the user interface tutorials.

<img src="screenshots/demo.gif?raw=true" alt="" width="240" border="10" />

## Getting Started

```groovy
compile 'com.github.popalay:tutors-kotlin:latest-version'
```
## Usage

```kotlin
val tutors = Tutors.create {
             textColorRes = android.R.color.white
             shadowColorRes = R.color.shadow
             textSizeRes = R.dimen.textNormal
             completeIconRes = R.drawable.ic_cross_24_white
             nextButtonTextRes = R.string.action_next
             completeButtonTextRes = R.string.action_got_it
             spacingRes = R.dimen.spacingNormal
             lineWidthRes = R.dimen.lineWidth
             cancelable = false
}

tutors.show(supportFragmentManager, view = toolbar, text = "It's a toolbar", isLast = true)
```

See [sample](sample/src/main/kotlin/com/github/popalay/tutorssample/MainActivity.kt).

License
-----

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
