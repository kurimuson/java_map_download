import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { provideRouter } from "@angular/router";
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MapControlPage } from './map-control.page';
import { KeyInputDialogComponent } from "./dialog/key-input-dialog.component";

@NgModule({
	imports: [
		CommonModule,
		FormsModule,
		ReactiveFormsModule,
		MatButtonModule,
		MatDialogModule,
		MatFormFieldModule,
		MatInputModule,
	],
	declarations: [
		MapControlPage,
		KeyInputDialogComponent,
	],
	providers: [
		provideRouter([
			{ path: '', component: MapControlPage },
		]),
	],
})
export class MapControlPageModule {
}
