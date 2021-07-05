import { NgModule } from '@angular/core';
import {MAT_FORM_FIELD_DEFAULT_OPTIONS, MatFormFieldDefaultOptions, MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatNativeDateModule} from '@angular/material/core';
import {MatListModule} from '@angular/material/list';
import {MatButtonModule} from '@angular/material/button';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatDialogModule} from '@angular/material/dialog';
import {MAT_SNACK_BAR_DEFAULT_OPTIONS, MatSnackBarConfig, MatSnackBarModule} from '@angular/material/snack-bar';
import {MatTableModule} from '@angular/material/table';
import {MatRadioModule} from '@angular/material/radio';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatIconModule} from '@angular/material/icon';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatSliderModule} from '@angular/material/slider';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatCardModule} from '@angular/material/card';
import {MatMenuModule} from '@angular/material/menu';
import {MatTabsModule} from '@angular/material/tabs';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatBadgeModule} from '@angular/material/badge';

const MAT_FORM_FIELD_APPEARANCE: MatFormFieldDefaultOptions = {
    appearance: 'outline'
};

const MAT_SNACK_BAR_SETTINGS: MatSnackBarConfig = {
    duration: 5000,
    verticalPosition: 'top',
};

const MATERIAL_MODULES = [
    MatFormFieldModule,
    MatDialogModule,
    MatInputModule,
    MatSelectModule,
    MatNativeDateModule,
    MatListModule,
    MatButtonModule,
    MatCheckboxModule,
    MatTableModule,
    MatRadioModule,
    MatProgressBarModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatDialogModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatTooltipModule,
    MatCardModule,
    MatMenuModule,
    MatTabsModule,
    MatSidenavModule,
    MatBadgeModule
];

@NgModule({
    exports: [...MATERIAL_MODULES],
    declarations: [],
    providers: [
        {provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: MAT_FORM_FIELD_APPEARANCE},
        {provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: MAT_SNACK_BAR_SETTINGS},
    ]
})
export class MaterialModule { }
