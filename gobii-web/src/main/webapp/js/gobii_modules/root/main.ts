import {bootstrap} from "@angular/platform-browser-dynamic";
import {provideStore} from '@ngrx/store';
import {ExtractorRoot} from "./app.extractorroot";
import reducer from '../store/reducers'

bootstrap(ExtractorRoot,
provideStore(reducer));
