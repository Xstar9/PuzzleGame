import 'dart:math';

import 'package:acountbook/utils/dj_constants.dart';
import 'package:flutter/material.dart';

class DJBottomNavigationBar extends StatefulWidget {
  final void Function(int index) onTap;
  final DJBottomNavigationBarOutItem centerItem;
  final List<Widget> items;
  final int currentIndex;
  const DJBottomNavigationBar({
    Key key,
    this.onTap,
    this.centerItem,
    this.items,
    this.currentIndex,
  }) : super(key: key);

  @override
  _DJBottomNavigationBarState createState() => _DJBottomNavigationBarState();
}

class _DJBottomNavigationBarState extends State<DJBottomNavigationBar> {
  @override
  Widget build(BuildContext context) {
    // 底部偏移，适配iPhoneX系列底部安全区域
    final double additionalBottomPadding =
        MediaQuery.of(context).padding.bottom;

    // items个数必须为偶数个
    int count = widget.items.length;
    if (count % 2 != 0) return null;

    Widget initExpandedWidget(int index, Widget child) {
      return Expanded(
        child: GestureDetector(
          onTap: () {
            // 点击barItem回调
            widget.onTap(index);
          },
          child: child,
        ),
      );
    }

    // 实际添加到导航栏的标签
    List<Widget> _resultItems = List();
    for (int i = 0; i < count; i++) {
      if(i == (count~/2)) {
        Widget resultItem = initExpandedWidget(-1, widget.centerItem);
        _resultItems.add(resultItem);
      }
      Widget tempItem = widget.items[i];
      Widget resultItem  = initExpandedWidget(i, tempItem);
      _resultItems.add(resultItem);
    }

    return Semantics(
      explicitChildNodes: true,
      child: Material(
        color: Colors.white,
        child: ConstrainedBox(
          constraints: BoxConstraints(
              minHeight: kBottomNavigationBarHeight + additionalBottomPadding),
          child: CustomPaint(
            painter: CustomPaintBottomBar(widget.centerItem.radius, 1),
            child: Material(
              type: MaterialType.transparency,
              child: Padding(
                padding: EdgeInsets.only(bottom: additionalBottomPadding),
                child: MediaQuery.removePadding(
                  context: context,
                  removeBottom: true,
                  child: Container(
                    height: kBottomNavigationBarHeight,
                    child: Flex(
                      direction: Axis.horizontal,
                      children: _resultItems,
                    ),
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class DJBottomNavigationBarOutItem extends StatefulWidget {
  final double radius;
  final String title;
  final IconData iconData;
  const DJBottomNavigationBarOutItem(
      {Key key, this.title, this.iconData, this.radius})
      : super(key: key);
  @override
  _DJBottomNavigationBarOutItemState createState() =>
      _DJBottomNavigationBarOutItemState();
}

class _DJBottomNavigationBarOutItemState
    extends State<DJBottomNavigationBarOutItem> {
  @override
  Widget build(BuildContext context) {
    return Container(
      child: Stack(
        alignment: Alignment.center,
        overflow: Overflow.visible,
        children: <Widget>[
          Positioned(
            bottom: 5.0,
            child: Column(children: <Widget>[
              Icon(
                widget.iconData,
                size: widget.radius * 2,
                color: Colors.red,
              ),
              Container(
                height: djBottomNavigationBarItemTextHeight,
                child: Text(
                  widget.title,
                  style: TextStyle(fontSize: 13.0, color: Colors.grey),
                ),
              )
            ]),
          ),
        ],
      ),
    );
  }
}

class CustomPaintBottomBar extends CustomPainter {
  final double radius;
  final double elevation;
  CustomPaintBottomBar(this.radius, this.elevation);
  // 绘图
  @override
  void paint(Canvas canvas, Size size) {
    double offsetY = kBottomNavigationBarHeight -
        djBottomNavigationBarItemTextHeight -
        djBottomNavigationBarBottomPadding -
        radius;
    Paint paint = Paint();
    paint.color = Colors.white; // 画笔颜色
    paint.isAntiAlias = true; // 是否抗锯齿
    paint.style = PaintingStyle.fill; //画笔样式:填充
    Path path = new Path();
    path.moveTo(0.0, 0.0);
    Rect rect = Rect.fromCircle(
      center: Offset(size.width * 0.5, offsetY),
      radius: radius,
    );
    path.arcTo(rect, djPI + asin(offsetY / radius),
        djPI - 2 * asin(offsetY / radius), false);
    path.lineTo(size.width, 0.0);
    path.lineTo(size.width, size.height);
    path.lineTo(0.0, size.height);
    path.close();
    canvas.drawPath(path, paint);

    if (elevation > 0) {
      double shadowRadius = radius - elevation;
      double shadowOffsetY = offsetY - elevation;
      Rect shadowRect = Rect.fromCircle(
        center: Offset(size.width * 0.5, offsetY),
        radius: shadowRadius,
      );
      Path shadowPath = Path.combine(
          PathOperation.difference,
          path,
          Path()
            ..moveTo(elevation, elevation)
            ..arcTo(shadowRect, djPI + asin(shadowOffsetY / shadowRadius),
                djPI - 2 * asin(shadowOffsetY / shadowRadius), false)
            ..lineTo(size.width, elevation)
            ..lineTo(size.width, size.height)
            ..lineTo(elevation, size.height)
            ..close());
      canvas.drawShadow(shadowPath, Color(0x229E9E9E), -elevation, false);
    }
  }
  // 刷新布局的时候告诉flutter 是否需要重绘
  @override
  bool shouldRepaint(CustomPainter oldDelegate) {
    return true;
  }
}

class DJBottomNavigationBarItem extends StatefulWidget {
  final String title;
  final IconData iconData;
  final bool selected;
  final VoidCallback onPressed;
  const DJBottomNavigationBarItem(
      {Key key, this.title, this.iconData, this.selected, this.onPressed})
      : super(key: key);
  @override
  _DJBottomNavigationBarItemState createState() =>
      _DJBottomNavigationBarItemState();
}

class _DJBottomNavigationBarItemState extends State<DJBottomNavigationBarItem> {
  final Color activeColor = Colors.lightBlue;
  final Color defaultColor = Colors.grey;
  @override
  Widget build(BuildContext context) {
    return ConstrainedBox(
      constraints: BoxConstraints(
        minWidth: double.infinity,
      ),
      child: Container(
        child: Padding(
          padding: EdgeInsets.only(
            bottom: 5.0,
            top: 6.0,
          ),
          child: Container(
            child: Column(
              children: <Widget>[
                Icon(
                  widget.iconData,
                  size: 26.0,
                  color: widget.selected ? activeColor : defaultColor,
                ),
                Container(
                  height: djBottomNavigationBarItemTextHeight,
                  child: Text(
                    widget.title,
                    style: TextStyle(
                      fontSize: 13.0,
                      color: widget.selected ? activeColor : defaultColor,
                    ),
                  ),
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}
