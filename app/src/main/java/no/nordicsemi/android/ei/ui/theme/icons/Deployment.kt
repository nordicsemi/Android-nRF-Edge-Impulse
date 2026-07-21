package no.nordicsemi.android.ei.ui.theme.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue", "UnusedReceiverParameter")
val Nordic.Icons.Deployment: ImageVector
  get() {
    if (_package != null) {
      return _package!!
    }
    _package =
      ImageVector.Builder(
          name = "package_2",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
        )
        .apply {
          path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.Companion.NonZero,
          ) {
            moveTo(11f, 19.43f)
            verticalLineTo(12.58f)
            lineTo(5f, 9.1f)
            verticalLineToRelative(6.85f)
            lineToRelative(6f, 3.48f)
            close()
            moveToRelative(2f, 0f)
            lineToRelative(6f, -3.48f)
            verticalLineTo(9.1f)
            lineToRelative(-6f, 3.47f)
            verticalLineToRelative(6.85f)
            close()
            moveToRelative(-2f, 2.3f)
            lineTo(4f, 17.7f)
            quadTo(3.53f, 17.43f, 3.26f, 16.98f)
            quadTo(3f, 16.52f, 3f, 15.98f)
            verticalLineTo(8.02f)
            quadToRelative(0f, -0.55f, 0.26f, -1f)
            reflectiveQuadTo(4f, 6.3f)
            lineTo(11f, 2.27f)
            quadTo(11.48f, 2f, 12f, 2f)
            quadToRelative(0.53f, 0f, 1f, 0.27f)
            lineTo(20f, 6.3f)
            quadToRelative(0.48f, 0.27f, 0.74f, 0.72f)
            reflectiveQuadToRelative(0.26f, 1f)
            verticalLineToRelative(7.95f)
            quadToRelative(0f, 0.55f, -0.26f, 1f)
            reflectiveQuadTo(20f, 17.7f)
            lineToRelative(-7f, 4.03f)
            quadTo(12.53f, 22f, 12f, 22f)
            reflectiveQuadTo(11f, 21.73f)
            close()
            moveTo(16f, 8.52f)
            lineToRelative(1.93f, -1.1f)
            lineTo(12f, 4f)
            lineTo(10.05f, 5.13f)
            lineTo(16f, 8.52f)
            close()
            moveToRelative(-4f, 2.33f)
            lineTo(13.95f, 9.73f)
            lineTo(8.03f, 6.3f)
            lineTo(6.08f, 7.43f)
            lineTo(12f, 10.85f)
            close()
          }
        }
        .build()
    return _package!!
  }

private var _package: ImageVector? = null