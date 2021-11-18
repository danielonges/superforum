import React, { useReducer } from 'react';
import axios from 'axios';
import UserContext from './userContext';
import userReducer from './userReducer';
import { ADMIN_USER, NORMAL_USER } from '../../utils/userTypes';
import {
    USER_LOADED,
    AUTH_ERROR,
    LOGIN_SUCCESS,
    LOGIN_FAIL,
    REGISTER_SUCCESS,
    LOGOUT,
    CLEAR_ERRORS,
    UPDATE_USER,
    UPDATE_USER_FAIL,
    GET_USERS,
    BLOCK_USER,
    UNBLOCK_USER
} from '../types';

const UserState = (props) => {
    const initialState = {
        isAuthenticated: null,
        loading: true,
        user: JSON.parse(localStorage.getItem('user')),
        users: null,
        error: null
    };

    const [state, dispatch] = useReducer(userReducer, initialState);

    const login = async (formData) => {
        const config = {
            headers: {
                'Content-Type': 'application/json'
            }
        };

        try {
            const res = await axios.post('/auth', formData, config);

            dispatch({
                type: LOGIN_SUCCESS,
                payload: res.data
            });
        } catch (error) {
            console.log(error.response.data.error);
            dispatch({
                type: LOGIN_FAIL,
                payload: error.response.data.error
            });
        }
    };

    const updateUser = async (user) => {
        try {
            await axios.put(`/user/${user.id}`, user);
            dispatch({
                type: UPDATE_USER,
                payload: user
            });
        } catch (error) {
            console.log(error.response.data.error);
            dispatch({
                type: UPDATE_USER_FAIL,
                payload: error.response.data.error
            });
        }
    };

    const getUsers = async (username) => {
        const res = await axios.get(`/user?username=${username}`);
        dispatch({
            type: GET_USERS,
            payload: res.data
        });
    };

    const blockUser = async (user, block, adminId) => {
        const config = {
            headers: {
                adminId: adminId
            }
        };

        try {
            await axios.put(`/user/block?block=${block}`, user, config);
            dispatch({
                type: block ? BLOCK_USER : UNBLOCK_USER,
                payload: user
            });
        } catch (error) {
            dispatch({
                type: UPDATE_USER_FAIL,
                payload: error.response.data.error
            });
        }
    };

    const logout = () =>
        dispatch({
            type: LOGOUT
        });

    const register = async (formData) => {
        const config = {
            headers: {
                'Content-Type': 'application/json'
            }
        };

        const res = await axios.post('/user', formData, config);

        dispatch({
            type: REGISTER_SUCCESS,
            payload: res.data
        });
    };

    const clearErrors = () => dispatch({ type: CLEAR_ERRORS });

    return (
        <UserContext.Provider
            value={{
                isAuthenticated: state.isAuthenticated,
                loading: state.loading,
                user: state.user,
                users: state.users,
                error: state.error,
                login,
                logout,
                register,
                updateUser,
                blockUser,
                getUsers,
                clearErrors
            }}
        >
            {props.children}
        </UserContext.Provider>
    );
};

export default UserState;
