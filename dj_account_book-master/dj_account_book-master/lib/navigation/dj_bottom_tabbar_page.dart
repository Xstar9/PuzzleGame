import 'package:acountbook/navigation/dj_bottom_navigation_bar.dart';
import 'package:acountbook/pages/charge/charge_page.dart';
import 'package:acountbook/pages/home/home_page.dart';
import 'package:acountbook/pages/user/user_center_page.dart';
import 'package:flutter/material.dart';

class DJBottomTabBarPage extends StatefulWidget {
  @override
  _DJBottomTabBarPageState createState() => _DJBottomTabBarPageState();
}

class _DJBottomTabBarPageState extends State<DJBottomTabBarPage> {
  // 当前选中的索引
  int currentIndex = 0;

  final PageController _pageController = PageController(
    initialPage: 0, // 默认选择第一个
  );

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: PageView(
        physics: NeverScrollableScrollPhysics(),
        controller: _pageController,
        children: <Widget>[
          DJHomePage(),
          DJUserCenterPage(),
        ],
      ),
      bottomNavigationBar: DJBottomNavigationBar(
        onTap: (index) {
          if (index == -1) {
            // 中间创建记账
            print(index);
            Navigator.of(context).push(PageRouteBuilder(
              opaque: false,
              pageBuilder: (BuildContext context, Animation animation,
                      Animation secondaryAnimation) =>
                  FadeTransition(
                      // 使用渐隐渐入过渡,
                      opacity: animation,
                      child: DJChargeAccountPage(),
                  ),
            ));
          } else {
            _pageController.jumpToPage(index);
            setState(() {
              currentIndex = index;
            });
          }
        },
        centerItem: DJBottomNavigationBarOutItem(
          radius: 28.0,
          title: "记账",
          iconData: Icons.add_circle,
        ),
        items: <Widget>[
          DJBottomNavigationBarItem(
            title: "首页",
            iconData: Icons.home,
            selected: currentIndex == 0,
          ),
          DJBottomNavigationBarItem(
            title: "个人中心",
            iconData: Icons.person,
            selected: currentIndex == 1,
          ),
        ],
        currentIndex: currentIndex,
      ),
    );
  }
}
