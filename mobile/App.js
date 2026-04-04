import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import OtpLoginScreen  from './src/screens/OtpLoginScreen';
import RegisterScreen  from './src/screens/RegisterScreen';
import HomeScreen      from './src/screens/HomeScreen';
import BuyPolicyScreen from './src/screens/BuyPolicyScreen';
import ClaimsScreen    from './src/screens/ClaimsScreen';

const Stack = createNativeStackNavigator();
const qc    = new QueryClient();

export default function App() {
  return (
    <QueryClientProvider client={qc}>
      <NavigationContainer>
        <Stack.Navigator screenOptions={{ headerShown: false }}>
          <Stack.Screen name="OtpLogin"  component={OtpLoginScreen}  />
          <Stack.Screen name="Register"  component={RegisterScreen}  />
          <Stack.Screen name="Home"      component={HomeScreen}      />
          <Stack.Screen name="BuyPolicy" component={BuyPolicyScreen} />
          <Stack.Screen name="Claims"    component={ClaimsScreen}    />
        </Stack.Navigator>
      </NavigationContainer>
    </QueryClientProvider>
  );
}
