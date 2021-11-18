import React, { Fragment } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

import CreateForum from './components/layout/CreateForum';
import EditForum from './components/layout/EditForum';
import CreateThread from './components/layout/CreateThread';
import Home from './components/layout/Home';
import Forum from './components/layout/Forum';
import ForumCategory from './components/layout/ForumCategory';
import ForumThread from './components/layout/ForumThread';
import SearchForums from './components/layout/SearchForums';
import Login from './components/auth/Login';
import Register from './components/auth/Register';
import PrivateRoute from './components/routing/PrivateRoute';

import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';

import AuthState from './context/user/UserState';
import AlertState from './context/alert/AlertState';
import ForumState from './context/forum/ForumState';
import Profile from './components/layout/Profile';
import ManageUsers from './components/layout/ManageUsers';

function App() {
    return (
        <AuthState>
            <AlertState>
                <ForumState>
                    <DndProvider backend={HTML5Backend}>
                        <Router>
                            <Fragment>
                                <Switch>
                                    <PrivateRoute
                                        exact
                                        path='/'
                                        component={Home}
                                    />
                                    <PrivateRoute
                                        exact
                                        path='/forum-category'
                                        component={ForumCategory}
                                    />
                                    <PrivateRoute
                                        exact
                                        path='/forum'
                                        component={Forum}
                                    />
                                    <PrivateRoute
                                        exact
                                        path='/forumthread'
                                        component={ForumThread}
                                    />
                                    <PrivateRoute
                                        exact
                                        path='/search-forums'
                                        component={SearchForums}
                                    />
                                    <PrivateRoute
                                        exact
                                        path='/create-forum'
                                        component={CreateForum}
                                    />
                                    <PrivateRoute
                                        exact
                                        path='/edit-forum'
                                        component={EditForum}
                                    />
                                    <PrivateRoute
                                        exact
                                        path='/create-thread'
                                        component={CreateThread}
                                    />
                                    <PrivateRoute
                                        exact
                                        path='/profile'
                                        component={Profile}
                                    />
                                    <PrivateRoute
                                        exact
                                        path='/manage-users'
                                        component={ManageUsers}
                                    />
                                    <Route
                                        exact
                                        path='/login'
                                        component={Login}
                                    />
                                    <Route
                                        exact
                                        path='/register'
                                        component={Register}
                                    />
                                </Switch>
                            </Fragment>
                        </Router>
                    </DndProvider>
                </ForumState>
            </AlertState>
        </AuthState>
    );
}

export default App;
