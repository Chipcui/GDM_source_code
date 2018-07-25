import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/take';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Effect, Actions} from '@ngrx/effects';
import {of} from 'rxjs/observable/of';
import {TreeStructureService} from '../../services/core/tree-structure-service';
import * as treeNodeActions from '../actions/treenode-action'
import {GobiiTreeNode} from "../../model/gobii-tree-node";

@Injectable()
export class TreeEffects {
    @Effect()
    initTreeNodes$ = this.actions$
        .ofType(treeNodeActions.INIT)
        .map((action: treeNodeActions.InitAction) => {
                let initialTreeNodes: GobiiTreeNode[] = this.treeStructureService.getInitialTree();
                return new treeNodeActions.InitTree(initialTreeNodes);
            }
        );


    @Effect()
    placeNodeInTree$ = this.actions$
        .ofType(treeNodeActions.PLACE_TREE_NODE)
        .map((action: treeNodeActions.PlaceTreeNodeAction) =>
            new treeNodeActions.ActivateForExtractAction(action.payload.getId())
        );

    @Effect()
    clearAll$ = this.actions$
        .ofType(treeNodeActions.CLEAR_ALL)
        .map((action: treeNodeActions.ClearAll) =>
            new treeNodeActions.InitAction()
        );


    constructor(private actions$: Actions,
                private treeStructureService: TreeStructureService,
                private router: Router) {
    }
}
