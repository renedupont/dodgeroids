# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces. For more details, see
#   https://developer.android.com/studio/build/shrink-code#decode-stack-trace
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn de.games.engine.graphics.AbstractBound$DetectionMethod
-dontwarn de.games.engine.graphics.AbstractBound
-dontwarn de.games.engine.graphics.Background
-dontwarn de.games.engine.graphics.Camera
-dontwarn de.games.engine.graphics.Color
-dontwarn de.games.engine.graphics.Font$FontStyle
-dontwarn de.games.engine.graphics.Font$HorizontalAlign
-dontwarn de.games.engine.graphics.Font$SizeType
-dontwarn de.games.engine.graphics.Font$Text
-dontwarn de.games.engine.graphics.Font$VerticalAlign
-dontwarn de.games.engine.graphics.Font
-dontwarn de.games.engine.graphics.GameRenderer
-dontwarn de.games.engine.graphics.Light$Id
-dontwarn de.games.engine.graphics.Light$Type
-dontwarn de.games.engine.graphics.Light
-dontwarn de.games.engine.graphics.Mesh
-dontwarn de.games.engine.graphics.Node
-dontwarn de.games.engine.graphics.RotationSettings
-dontwarn de.games.engine.graphics.SphereBound
-dontwarn de.games.engine.graphics.Sprite
-dontwarn de.games.engine.graphics.Texture$TextureFilter
-dontwarn de.games.engine.graphics.Texture$TextureWrap
-dontwarn de.games.engine.graphics.Texture
-dontwarn de.games.engine.graphics.Vector
-dontwarn de.games.engine.levels.AbstractLevelFactory
-dontwarn de.games.engine.objects.AbstractGameObject
-dontwarn de.games.engine.objects.GameObjectChain
-dontwarn de.games.engine.scenes.Scene

-keep class de.games.engine.graphics.AbstractBound$DetectionMethod { *; }
-keep class de.games.engine.graphics.AbstractBound { *; }
-keep class de.games.engine.graphics.Background { *; }
-keep class de.games.engine.graphics.Camera { *; }
-keep class de.games.engine.graphics.Color { *; }
-keep class de.games.engine.graphics.Font$FontStyle { *; }
-keep class de.games.engine.graphics.Font$HorizontalAlign { *; }
-keep class de.games.engine.graphics.Font$SizeType { *; }
-keep class de.games.engine.graphics.Font$Text { *; }
-keep class de.games.engine.graphics.Font$VerticalAlign { *; }
-keep class de.games.engine.graphics.Font { *; }
-keep class de.games.engine.graphics.GameRenderer { *; }
-keep class de.games.engine.graphics.Light$Id { *; }
-keep class de.games.engine.graphics.Light$Type { *; }
-keep class de.games.engine.graphics.Light { *; }
-keep class de.games.engine.graphics.Mesh { *; }
-keep class de.games.engine.graphics.Node { *; }
-keep class de.games.engine.graphics.RotationSettings { *; }
-keep class de.games.engine.graphics.SphereBound { *; }
-keep class de.games.engine.graphics.Sprite { *; }
-keep class de.games.engine.graphics.Texture$TextureFilter { *; }
-keep class de.games.engine.graphics.Texture$TextureWrap { *; }
-keep class de.games.engine.graphics.Texture { *; }
-keep class de.games.engine.graphics.Vector { *; }
-keep class de.games.engine.levels.AbstractLevelFactory { *; }
-keep class de.games.engine.objects.AbstractGameObject { *; }
-keep class de.games.engine.objects.GameObjectChain { *; }
-keep class de.games.engine.scenes.Scene { *; }