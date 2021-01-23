import 'package:acountbook/navigation/dj_bottom_tabbar_page.dart';
import 'package:flutter/material.dart';

void main() => runApp(DJAccountBookApp());
class DJAccountBookApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.lightBlue,
        splashColor: Colors.transparent,
        highlightColor: Colors.transparent,
      ),
      home: DJBottomTabBarPage(),
    );
  }
}
