import 'package:flutter/material.dart';

class DJChargeAccountPage extends StatefulWidget {
  @override
  _DJChargeAccountPageState createState() => _DJChargeAccountPageState();
}

class _DJChargeAccountPageState extends State<DJChargeAccountPage> {
  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: choices.length,
      child: Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.white,
          title: const Text('日常账单'),
          bottom: TabBar(
            isScrollable: false,
            tabs: choices.map<Widget>((Choice choice) {
              return Tab(
                text: choice.title,
              );
            }).toList(),
          ),
        ),
        body:Container(
          color: Colors.white,
          child: Column(
            children: <Widget>[
              Padding(
                padding: EdgeInsets.only(
                  top: 15.0
                ),
              ),
              Flex(
                direction: Axis.horizontal,
                children: <Widget>[
                  Container(
                    width: 50.0,
                    child: Icon(Icons.ac_unit,size: 24,),
                  ),
                  Text(
                    "夜宵",
                    style: TextStyle(
                      color: Colors.black,
                    ),
                  ),
                  Expanded(
                      child: Container(
                          height: 60,
                      ),
                  ),
                  Container(
                    width: 200.0,
                    child: Text(
                        "9999999.00",
                        textAlign: TextAlign.right,
                        style: TextStyle(
                          fontSize: 26,
                          fontWeight: FontWeight.bold,
                          color: Colors.black,
                        ),
                    ),
                  ),
                  Padding(
                    padding: EdgeInsets.only(right: 10.0),
                  )
                ],
              ),
              Padding(
                padding: EdgeInsets.only(
                  left: 10,
                  right: 10,
                ),
                child: Divider(
                  height: 2.0,
                  color: Colors.grey,
                ),
              )
            ],
          ),
        )
      ),
    );
  }
}


class Choice {
  const Choice({ this.title, this.icon });
  final String title;
  final IconData icon;
}

const List<Choice> choices = <Choice>[
  Choice(title: '支出'),
  Choice(title: '收入'),
  Choice(title: '借还'),
];

class ChoiceCard extends StatelessWidget {
  const ChoiceCard({ Key key, this.choice }) : super(key: key);

  final Choice choice;

  @override
  Widget build(BuildContext context) {
    final TextStyle textStyle = Theme.of(context).textTheme.headline;
    return Card(
      color: Colors.white,
      child: Center(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            Icon(choice.icon, size: 128.0, color: textStyle.color),
            Text(choice.title, style: textStyle),
          ],
        ),
      ),
    );
  }
}
