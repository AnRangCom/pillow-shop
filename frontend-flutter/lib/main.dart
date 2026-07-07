import 'package:flutter/material.dart';
import 'screens/auth/login_screen.dart';
import 'screens/auth/register_screen.dart';
import 'screens/home/home_screen.dart';
import 'screens/pillow/pillow_list_screen.dart';
import 'screens/pillow/pillow_detail_screen.dart';
import 'screens/chat/chat_screen.dart';
import 'config/theme.dart';

void main() {
  runApp(const PillowShopApp());
}

class PillowShopApp extends StatelessWidget {
  const PillowShopApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Pillow Shop',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
        primaryColor: const Color(AppTheme.primaryColor),
        useMaterial3: true,
      ),
      initialRoute: '/',
      onGenerateRoute: (settings) {
        switch (settings.name) {
          case '/':
            return MaterialPageRoute(builder: (_) => const HomeScreen());
          case '/login':
            return MaterialPageRoute(builder: (_) => const LoginScreen());
          case '/register':
            return MaterialPageRoute(builder: (_) => const RegisterScreen());
          case '/pillows':
            return MaterialPageRoute(builder: (_) => const PillowListScreen());
          case '/pillow-detail':
            final pillowId = settings.arguments as int;
            return MaterialPageRoute(
              builder: (_) => PillowDetailScreen(pillowId: pillowId),
            );
          case '/chat':
            return MaterialPageRoute(builder: (_) => const ChatScreen());
          default:
            return MaterialPageRoute(builder: (_) => const HomeScreen());
        }
      },
    );
  }
}
