[![Android CI](https://github.com/Fbada006/CoinTicker/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/Fbada006/CoinTicker/actions/workflows/build.yml)

# CoinTicker

A simple android application that queries crypto data, caches it, and displays it on screen.
This app is built using the Model-View-ViewModel (MVVM) architecture in 100% Kotlin. It has the
following features and screens:

### 1. Main screen

This screen is used for listing all the coins queried from the API. It shows the following:

- A list of all the coins from the db after being fetched from the API. Each coin has a name, image (defaulting to a dollar sign on error), date last
  updated, and a fav icon
- A search field to perform a search on the items
- Every item in the list can have a favorite icon to add or remove items to favorites
- A favorite button on the action bar to filter items by favorites
- Each item is clickable

### 2. Favorites screen

This screen shows a list of all favorite coins from the db. Each item is clickable as well and has all the information on the main screen except
for the search and fav buttons.

### 2. Details screen

This screen shows the details of the clicked item. It shows the following:

- The name of the coin
- The icon of the coin, with a default to the dollar sign if error
- The coin symbol
- The USD price
- The exchange rates for the euro and the sterling pound

# Prerequisites

The app was built using Android Studio Flamingo | 2022.2.1 Patch 2 so try running at least that version of Android studio. As for
testing devices,
ensure they are at least running Android API 24 and above. Running UI tests can be done straight from the Android Studio
UI.

## Table of contents

- [Architecture](#architecture)
- [Libraries](#libraries)
- [Testing](#testing)
- [Extras](#extras)

## Architecture

The app uses the MVVM architecture because it is a nice and simple architecture that makes testing and maintenance
easier. It was also chosen
because it is a popular choice meaning a new developer can pick it up easily making for smooth transitions between
teams. There is also the
added benefit of using a `ViewModel` to survive configuration changes. `Kotlin Flow` and `Coroutines` have been used to
monitor data and
state changes in the app making for a smooth user experience. The full explanation of the architecture used can be found
[here](https://developer.android.com/topic/architecture) on the official Android developer website. For added ease of
maintenance and extra abstraction, the repository pattern with use-cases is also used, which further helps keep things cleaner.

## Libraries

This app makes use of the following libraries:

- [Jetpack](https://developer.android.com/jetpack)🚀
    - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Manage UI data to survive
      configuration changes
      and is lifecycle-aware
    - [Compose](https://developer.android.com/jetpack/compose) - Android’s modern toolkit for building native UI.
    - [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) - Handle everything needed for in-app navigation
    - [Room DB](https://developer.android.com/topic/libraries/architecture/room) - Fluent SQLite database access
- [Dagger Hilt](https://dagger.dev/hilt/) - A fast dependency injector for Android and Java.
- [Retrofit](https://square.github.io/retrofit/) - type safe http client with coroutines support
- [Moshi](https://github.com/square/moshi) - A modern JSON library for Android, Java and Kotlin
- [okhttp-logging-interceptor](https://github.com/square/okhttp/blob/master/okhttp-logging-interceptor/README.md) -
  logging HTTP request related data.
- [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Library Support for coroutines
- [Material Design](https://material.io/develop/android/docs/getting-started/) - build awesome beautiful UIs.🔥🔥
- [Timber](https://github.com/JakeWharton/timber) - A logger with a small, extensible API which provides utility on top
  of Android's normal Log class.
- [Truth](https://truth.dev/) - A library for performing assertions in tests
- [Mockk](https://mockk.io/) - A mocking framework for testing in Android
- [Robolectric ](http://robolectric.org/) - For fast and reliable unit tests to Android.
- [Turbine](https://github.com/cashapp/turbine) - Turbine is a small testing library for `kotlinx.coroutines Flow`
- [Coil](https://coil-kt.github.io/coil/compose/) - Hassle-free image loading with free caching

## Testing

There are unit tests to test the logic of the application. To run the tests, refer to the section above.

## Extras

#### CI-Pipeline

The app also uses GitHub actions to build the app and runs whenever a new pull request or merge to the `master` branch
happens.
The pipeline also runs unit tests. The status of the build is displayed at the top of this file.

#### Challenges and improvements

1. The biggest challenge is that this API does not support pagination, which means the app has to load all the coins first before saving to the db
   and then querying it to display the data since that is the source of truth. This means the app is a few seconds slower as opposed to when
   pagination
   is used. Through the use of Kotlin Flow and Jetpack Compose's `LazyColumn`, this has been more or less mitigated and the user interface is smooth.
2. Another challenge in the app has been how to handle the favorite feature from the main list while at the same
   time providing a
   smooth user experience. One of the things I did was refresh the entire list once the favorite button was clicked for
   an item but the list
   appeared to jump. This forced a rethink where I solved it by working on a background thread to save the favorite in
   the db
   then update the list in memory once the favorite operation is completed, which triggers a recomposition of the list
   in a smooth way.


```
Copyright (c) 2023 Ferdinand Bada

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
```
