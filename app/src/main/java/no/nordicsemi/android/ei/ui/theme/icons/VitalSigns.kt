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
val Nordic.Icons.VitalSigns: ImageVector
    get() {
        if (_vitalSigns != null) {
            return _vitalSigns!!
        }
        _vitalSigns =
            ImageVector.Builder(
                    name = "vital_signs",
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
                        pathFillType = PathFillType.NonZero,
                    ) {
                        moveTo(8.15f, 19.73f)
                        quadTo(7.78f, 19.45f, 7.6f, 19.02f)
                        lineTo(5.3f, 13f)
                        horizontalLineTo(1f)
                        verticalLineTo(11f)
                        horizontalLineTo(6.7f)
                        lineTo(9f, 17.1f)
                        lineTo(13.6f, 4.97f)
                        quadToRelative(0.17f, -0.43f, 0.55f, -0.7f)
                        reflectiveQuadTo(15f, 4f)
                        reflectiveQuadToRelative(0.85f, 0.27f)
                        reflectiveQuadToRelative(0.55f, 0.7f)
                        lineTo(18.7f, 11f)
                        horizontalLineTo(23f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(17.3f)
                        lineTo(15f, 6.9f)
                        lineTo(10.4f, 19.02f)
                        quadToRelative(-0.17f, 0.43f, -0.55f, 0.7f)
                        reflectiveQuadTo(9f, 20f)
                        reflectiveQuadTo(8.15f, 19.73f)
                        close()
                    }
                }
                .build()
        return _vitalSigns!!
    }

private var _vitalSigns: ImageVector? = null