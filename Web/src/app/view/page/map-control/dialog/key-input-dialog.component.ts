import { Component, Inject, OnInit } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormControl, Validators } from "@angular/forms";

@Component({
	selector: 'com-key-input-dialog',
	styleUrls: ['key-input-dialog.component.scss'],
	templateUrl: 'key-input-dialog.component.html',
})
export class KeyInputDialogComponent implements OnInit {

	key = new FormControl('', [Validators.required]);

	constructor(
		@Inject(MAT_DIALOG_DATA)
		private data: { defaultKey: string, callback: (key: string) => void },
		private dialogRef: MatDialogRef<KeyInputDialogComponent>,
	) {
	}

	ngOnInit(): void {
		this.key.setValue(this.data.defaultKey);
	}

	getErrorMessage(): String {
		return this.key.hasError('required') ? 'Key不能为空' : '';
	}

	submit(): void {
		if (this.key.hasError('required') || this.key.value == null) {
			return;
		}
		this.data.callback(this.key.value);
		this.dialogRef.close();
	}

}
