import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SetupGameComponent} from './start/setup-game.component';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {path: '', component: SetupGameComponent}
];

@NgModule({
  declarations: [SetupGameComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ]
})
export class StartModule {
}
