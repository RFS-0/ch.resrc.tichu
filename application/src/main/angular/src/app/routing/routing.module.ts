import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {ROUTES_PROVIDER} from './routes.provider';


@NgModule({
  imports: [RouterModule.forRoot([])],
  exports: [RouterModule],
  providers: [ROUTES_PROVIDER]
})
export class RoutingModule {
}
