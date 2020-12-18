import {LoadChildren, Router, Routes} from '@angular/router';
import {APP_INITIALIZER, Injector} from '@angular/core';

function createContextRoutes(
  path: string,
  loadChildrenCallback: LoadChildren,
  data: any = {}
): Routes {
  return [
    {
      path,
      loadChildren: loadChildrenCallback,
      canActivate: [],
      data: {
        ...data
      }
    },
  ];
}

const appRoutes: Routes = [
  ...createContextRoutes('start', () => import('../start/start.module').then(mod => mod.StartModule)),
  {path: '', redirectTo: 'setup', pathMatch: 'full'},
  {path: '**', redirectTo: 'error-page', pathMatch: 'full'}
];

export function configRoutes(injector: Injector) {
  return () => {
    const router: Router = injector.get(Router);
    router.resetConfig(appRoutes);
  };
}

export const ROUTES_PROVIDER = {provide: APP_INITIALIZER, useFactory: configRoutes, deps: [Injector], multi: true};
